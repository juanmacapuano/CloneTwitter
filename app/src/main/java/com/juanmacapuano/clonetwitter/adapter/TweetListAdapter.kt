package com.juanmacapuano.clonetwitter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juanmacapuano.clonetwitter.databinding.FragmentTweetItemBinding
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet

class TweetListAdapter(private val clickListener: (Tweet) -> Unit, private val context: Context) :
    RecyclerView.Adapter<TweetListAdapter.TweetListHolder>() {
    private val listTweets = ArrayList<Tweet>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TweetListAdapter.TweetListHolder {
        val binding = FragmentTweetItemBinding.inflate(LayoutInflater.from(parent.context))
        return TweetListHolder(binding)
    }

    override fun onBindViewHolder(holder: TweetListAdapter.TweetListHolder, position: Int) {
        holder.bind(listTweets[position], clickListener, context)
    }

    override fun getItemCount(): Int {
        return listTweets.size
    }

    fun setListTweet(listTweet: List<Tweet>) {
        listTweets.clear()
        listTweets.addAll(listTweet)
        notifyDataSetChanged()
    }

    class TweetListHolder(private val binding: FragmentTweetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tweet: Tweet, clickListener: (Tweet) -> Unit, context: Context) {
            binding.tvUserNameTweet.text = tweet.user.userName
            binding.tvMessageTweet.text = tweet.message
            binding.tvLikeNumberTweet.text = tweet.likes.size.toString()
        }
    }

}