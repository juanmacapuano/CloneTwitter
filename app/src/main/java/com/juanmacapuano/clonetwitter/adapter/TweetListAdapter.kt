package com.juanmacapuano.clonetwitter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.juanmacapuano.clonetwitter.R
import com.juanmacapuano.clonetwitter.databinding.FragmentTweetItemBinding
import com.juanmacapuano.clonetwitter.service.data.tweets.Tweet
import com.juanmacapuano.clonetwitter.viewModel.ViewModelTweets

class TweetListAdapter(private val clickListener: (Tweet) -> Unit,
                       private val context: Context) :
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

            if(tweet.user.photoUrl != "") {
                Glide.with(context)
                    .load("https://www.minitwitter.com/apiv1/uploads/photos/" +
                    tweet.user.photoUrl)
                    .into(binding.ivUserPhotoTweet)
            }

            for (item in tweet.likes) {
                if (item.userName == tweet.user.userName) {
                    Glide.with(context)
                        .load(R.drawable.ic_favorite_24dp)
                        .into(binding.ivLikeTweet)
                    binding.tvLikeNumberTweet.resources.getColor(R.color.purple_500)
                }
            }

            binding.executePendingBindings()
        }


    }

}